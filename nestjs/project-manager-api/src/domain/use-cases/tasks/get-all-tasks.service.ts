import { Injectable } from '@nestjs/common';
import { BaseUseCase } from '../base-use-case';
import { UsersRepositoryService } from 'src/infrastructure/database/repositories/users.repository.service';
import { TasksRepositoryService } from 'src/infrastructure/database/repositories/tasks.repository.service';

@Injectable()
export class GetAllTasksService implements BaseUseCase{
    constructor(private readonly tasksRepository: TasksRepositoryService,
        private readonly usersRepository: UsersRepositoryService
    ) {}

    async execute(payload: {userId  : number}): Promise<any> {
        const userData =  await this.usersRepository.findById(payload.userId);
        if (!userData) {
            throw new Error('User not found');
        }   
        const tasks = await this.tasksRepository.findAll(payload.userId);
        if (!tasks) {
            throw new Error('No tasks found for this user');
        }
        return tasks;
    }
}
