#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <limits.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <dirent.h>
#include <unistd.h>
#include <alloca.h>
#include <libgen.h>
#include <float.h>
#include <math.h>
#include <stdbool.h>
#pragma FLT_ROUNDS = 1
#define INODES_SIZE 255
#define _XOPEN_SOURCE 500
#define FSPATH "/home"

//Для вывода полного пути
char *script_name = NULL;
char actualpath [PATH_MAX + 1];
char *ptr;

struct stat globalDev;
ino_t *mas=NULL;

void writeError(char *theDir){
	ptr = realpath(theDir, actualpath);
	if (strcmp(strerror(errno),"Success") != 0)
	fprintf(stderr,"%s: %s %s\n",script_name, strerror(errno), (theDir)? ptr : "");
}

long long process_directory(char *theDir,long long *sizeblock,long long *maslen)
{
	DIR *dir = NULL;
	struct dirent entry;
	struct dirent *entryPtr = NULL;
	int retval = 0;
	*sizeblock = 0;
	long long count = 0;
	char *pathName = malloc(strlen(theDir) + NAME_MAX + 3);
	dir = opendir( theDir );
	count+=4096;
	*sizeblock+=8;
	if (dir == NULL)
	{
		writeError(theDir);
		return 0;
	}
	retval = readdir_r( dir, &entry , &entryPtr );
	if (retval > 0)
		writeError(theDir);
	while ( entryPtr != NULL)
	{
		struct stat entryInfo;
		if ( ( strcmp( entry.d_name, "." ) == 0) ||
             ( strcmp( entry.d_name, "..") == 0 ) )
		{
			retval = readdir_r( dir, &entry, &entryPtr );
			if ( retval > 0)
				writeError(theDir);
			continue;
		}
		(void)strcpy( pathName, theDir);
		(void)strcat( pathName, "/" );
		(void)strcat( pathName, entry.d_name);
		if (lstat( pathName, &entryInfo) == 0)
		{
			if( S_ISLNK(( entryInfo.st_mode)&& (globalDev.st_dev==entryInfo.st_dev) ) ) {
                char targetName[PATH_MAX + 1];
                if( readlink( pathName, targetName, PATH_MAX ) != -1 ) {
                    count+=strlen(targetName);
					*sizeblock+=8;
                }
				else
				{
					writeError(theDir);
				}
				retval = readdir_r( dir, &entry, &entryPtr);
                if (retval > 0)
                    writeError(theDir);
				continue;
            }
			if ( S_ISDIR( entryInfo.st_mode ) && (globalDev.st_dev==entryInfo.st_dev))
			{
				int temp=*sizeblock;
				count += process_directory( pathName,sizeblock,maslen );
				*sizeblock+=temp;
			} else if ( S_ISREG( entryInfo.st_mode ) && (globalDev.st_dev==entryInfo.st_dev) )
            {
                if ((int)entryInfo.st_nlink>1)
                {
                    bool fl = true;
                    int i=0;
                    for (i=0;i<*maslen;i++)
                    {
                        if (mas[i]==entryInfo.st_ino){
                            fl=false;
                        }
                    }
                    if (fl)
                    {
                        mas=(ino_t*)realloc(mas,(*maslen+1)*sizeof(ino_t));
                        mas[*maslen]=entryInfo.st_ino;
                        *maslen+=1;
                        count+=(long long)entryInfo.st_size;
                        *sizeblock+=(long long)entryInfo.st_blocks;

                    }
                }
                else
                {
                    count+=( long long )entryInfo.st_size;
                    *sizeblock+=(long long)entryInfo.st_blocks;
                }
            }
        } else {
            writeError(theDir);
        }
        retval = readdir_r( dir, &entry, &entryPtr);
        if (retval > 0)
            writeError(theDir);
    }
    long long sizeout;
    sizeout = 512*(*sizeblock);
    ptr = realpath(theDir, actualpath);
    printf("%s %lld %lld \n",ptr,count,sizeout);
    int closeErr;
    closeErr = closedir( dir );
    if ( closeErr != 0){
        writeError(theDir);
    }
    free(entryPtr);
    free(pathName);
    return count;
}

int main( int argc, char **argv )
{
	long long count = 0;
	long long sb;
	int err;
	int len=0;
	err=stat(argv[1],&globalDev);

	script_name = basename(argv[0]);
	count += process_directory( argv[1], &sb,&len);
	double res;
	long long size=512*sb;
	res=(double) count / size;
	res=res*100;
	free(mas);
	printf("%lld ",count);
	printf("%lld ",size);
	printf("%.2f%% \n",res);
	return EXIT_SUCCESS;
}
