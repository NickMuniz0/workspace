import { Module } from '@nestjs/common';
import { UpdateTaskService } from './update-task.service';
import { GetTaskByIdService } from './get-task-by-id.service';
import { GetAllTasksService } from './get-all-tasks.service';
import { CreateTaskService } from './create-task.service';
import { DatabaseModule } from 'src/infrastructure/database/database.module';


@Module({
  imports: [DatabaseModule],  
  providers: [CreateTaskService, GetAllTasksService, GetTaskByIdService, UpdateTaskService],
  exports: [CreateTaskService, GetAllTasksService, GetTaskByIdService, UpdateTaskService],
})
export class TasksModule {}
