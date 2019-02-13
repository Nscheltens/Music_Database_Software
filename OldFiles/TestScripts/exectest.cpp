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
		int fd = open(argv[2], O_RDWR | O_CREAT | O_APPEND, S_IRUSR | S_IWUSR);
		dup2(fd, 1);   // make stdout go to file
		dup2(fd, 2);   // make stderr go to file - you may choose to not do this
		               // or perhaps send stderr to another file
		close(fd);     // fd no longer needed - the dup'ed handles are sufficient
		execl("/bin/ls", "/bin/ls", argv[1], (char *) 0);
	}
	else{
		fprintf(stderr, "PID %d: waiting for child\n", getpid());
		waitpid(child_pid, &statval, WUNTRACED
			#ifdef WCONTINUED
				| WCONTINUED 
			#endif
			);
		if(WIFEXITED(statval)){
			fprintf(stderr, "Child's exit code %d\n",WEXITSTATUS(statval));
		}
		else{
			fprintf(stderr, "Child did not terminate with exit\n");
		}
	}
}
