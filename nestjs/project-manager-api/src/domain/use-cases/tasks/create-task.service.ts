import { Injectable } from '@nestjs/common';
import { BaseUseCase } from '../base-use-case';
import { UsersRepositoryService } from 'src/infrastructure/database/repositories/users.repository.service';
import { TasksRepositoryService } from 'src/infrastructure/database/repositories/tasks.repository.service';
import { ProjectsRepositoryService } from 'src/infrastructure/database/repositories/projects.repository.service';
import { CreateTasksDto } from 'src/gateways/controllers/tasks/dtos/create-tasks.dto';
import { Itask } from 'src/domain/interfaces/task.interface';

@Injectable()
export class CreateTaskService implements BaseUseCase{
    constructor(
        private readonly usersRepository: UsersRepositoryService,
        private readonly tasksRepository: TasksRepositoryService,
        private readonly projectsRepository: ProjectsRepositoryService
        
    ) {}

    async execute(payload: {task:CreateTasksDto; userId:number}): Promise<Itask> {
        const userData =  await this.usersRepository.findById(payload.userId);
        if (!userData) {
            throw new Error('User not found');
        }
        const projectData = await this.projectsRepository.findById(payload.userId, parseInt(payload.task.projectId));
        if (!projectData) {
            throw new Error('Project not found or access denied');
        }
        const newTask = await this.tasksRepository.add({
            name: payload.task.name,
            status: payload.task.status,
            user: {id: projectData?.id},
            project: {id: projectData.id}
        });
        if (!newTask) {
            throw new Error('Failed to create task');
        }
        return newTask;
    }
}
