import { Injectable } from '@nestjs/common';
import { BaseUseCase } from '../base-use-case';
import { TasksRepositoryService } from 'src/infrastructure/database/repositories/tasks.repository.service';
import { UsersRepositoryService } from 'src/infrastructure/database/repositories/users.repository.service';
import { Itask } from 'src/domain/interfaces/task.interface';

@Injectable()
export class GetTaskByIdService implements BaseUseCase{
    constructor(private readonly tasksRepository: TasksRepositoryService,
        private readonly usersRepository: UsersRepositoryService
    ) {}

    async execute(payload: {userId  : number; taskId: number}): Promise<Itask> {
        const userData =  await this.usersRepository.findById(payload.userId);
        if (!userData) {
            throw new Error('User not found');
        }   
        const task = await this.tasksRepository.findById(userData.id, payload.taskId);
        if (!task) {
            throw new Error('Task not found for this user');
        }
        return task;
    }
}
