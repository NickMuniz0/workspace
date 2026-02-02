import { Body, Controller, Get, Param, Post, Req } from '@nestjs/common';
import { CreateTaskService } from 'src/domain/use-cases/tasks/create-task.service';
import { GetAllTasksService } from 'src/domain/use-cases/tasks/get-all-tasks.service';
import { GetTaskByIdService } from 'src/domain/use-cases/tasks/get-task-by-id.service';
import { CreateTasksDto } from './dtos/create-tasks.dto';


const loggerUser = 1
@Controller('tasks')
export class TasksController {

    constructor(
        private readonly getAllTasksUseCase: GetAllTasksService,
        private readonly getTaskByIdUseCase: GetTaskByIdService,
        private readonly createTaskUseCase: CreateTaskService
    ) { }

    @Get()
    findAll() {
        try{
        return  this.getAllTasksUseCase.execute({userId:loggerUser});
        }catch(error){
            console.log(error);
        }
    }        
    
    @Get(':id')
    findById(@Req() request, @Param('id') taskId: number) {
        try{
            return this.getTaskByIdUseCase.execute({taskId:taskId, userId:loggerUser});
        }catch(error){
            console.log(error);
        }
    }

    @Post()
    create(@Req() request, @Body() taskData: CreateTasksDto) {
        try{
            return this.createTaskUseCase.execute({task:taskData, userId:loggerUser});
        }catch(error){
            console.log(error);
        }
    }


}