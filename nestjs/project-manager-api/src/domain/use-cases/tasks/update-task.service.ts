import { Injectable, NotFoundException } from '@nestjs/common';
import { TaskEntity } from 'src/infrastructure/database/entities/task.entity';
import { TasksRepositoryService } from 'src/infrastructure/database/repositories/tasks.repository.service';
import { UsersRepositoryService } from 'src/infrastructure/database/repositories/users.repository.service';
import { BaseUseCase } from '../base-use-case';
import { UpdateTasksDto } from 'src/gateways/controllers/tasks/dtos/update-tasks.dto';

@Injectable()
export class UpdateTaskService implements BaseUseCase{
    constructor(private readonly tasksRepository: TasksRepositoryService,
        private readonly usersRepository: UsersRepositoryService
    ) {}
    async execute(payload: {userId: number, task: UpdateTasksDto}): Promise<unknown> {
        const userData = await this.usersRepository.findById(payload.userId);
        if(!userData){
            throw new NotFoundException(`User with ID ${payload.userId} not found`);
        }

        await this.tasksRepository.updateById( userData.id, payload.task);
        return this.tasksRepository.findById( userData.id,payload.task.id)  ;;

    }

}