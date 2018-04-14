/*
 * OS Assignment #3
 *
 * @file sem.c
 */

#include "sem.h"
#include <stdlib.h>

struct test_semaphore
{
  int             value;
  pthread_mutex_t mutex;
  pthread_cond_t  cond;
};

tsem_t *
tsem_new (int value)
{
  tsem_t *sem;

  sem = malloc (sizeof (tsem_t));
  if (!sem)
    return NULL;

  sem->value = value;
  pthread_mutex_init (&sem->mutex, NULL);
  pthread_cond_init (&sem->cond, NULL);

  return sem;
}

void
tsem_free (tsem_t *sem)
{
  if (!sem)
    return;

  pthread_cond_destroy (&sem->cond);
  pthread_mutex_destroy (&sem->mutex);
  free (sem);
}

void
tsem_wait (tsem_t *sem)
{
  if (!sem)
    return;

  pthread_mutex_lock (&sem->mutex);
  sem->value--;
  if (sem->value < 0)
    pthread_cond_wait (&sem->cond, &sem->mutex);
  pthread_mutex_unlock (&sem->mutex);
}

int
tsem_try_wait (tsem_t *sem)
{
	if (!sem)//if null
		return -1;
	pthread_mutex_lock(&sem->mutex);//lock mutext
	if(sem->value >= 1){ //
		sem->value--; // P
  		pthread_mutex_unlock (&sem->mutex);//unlock mutex
		return 0;
	}
	else{
  		pthread_mutex_unlock (&sem->mutex);//unlock mutex
		return 1;
	}
}

void tsem_signal (tsem_t *sem)
{
  if (!sem)
    return;

  pthread_mutex_lock (&sem->mutex);
  sem->value++;
  if (sem->value <= 0)
    pthread_cond_signal (&sem->cond);
  pthread_mutex_unlock (&sem->mutex);  
}
