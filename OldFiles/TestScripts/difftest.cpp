#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <stdlib.h>
#include <stdio.h>

int main(int argc, char *argv[]){

	// Check for correct amount of arguments {program, ls directory, file to write to}
	if(argc != 3){
		fprintf(stderr, "wrong amount of arguments!");
		exit(-1);
	}
	int number, statval;
	int child_pid = fork();
	fprintf(stderr ,"%d: I'm the parent! \n", getpid());

	if(child_pid == -1){
		printf("could not fork! \n");
		exit(1);
	}
	else if (child_pid == 0){
		// child
		int fd = open("difflog", O_RDWR | O_CREAT | O_APPEND, S_IRUSR | S_IWUSR);
		dup2(fd, 1);   // make stdout go to file
		dup2(fd, 2);   // make stderr go to file - you may choose to not do this
		               // or perhaps send stderr to another file
		close(fd);     // fd no longer needed - the dup'ed handles are sufficient
		execl("/usr/bin/diff", "/usr/bin/diff", argv[1], argv[2], (char *) 0);
	}
	else{
		fprintf(stderr, "PID %d: waiting for child\n", getpid());
		waitpid(child_pid, &statval, WUNTRACED
			#ifdef WCONTINUED
				| WCONTINUED 
			#endif
			);
		if(WIFEXITED(statval)){
			if(WEXITSTATUS(statval) == 0){
				fprintf(stderr, "files are the same \n");
			}
			else if(WEXITSTATUS(statval) == 1){
				fprintf(stderr, "files are diffrent\n");
				//to do:
				//add exec  program to add items into database
				//move new file to become the standard
			}
			else{
				fprintf(stderr, "error occured %d\n", WEXITSTATUS(statval));
			}
		}
		else{
			fprintf(stderr, "Child did not terminate with exit\n");
		}
	}
}
