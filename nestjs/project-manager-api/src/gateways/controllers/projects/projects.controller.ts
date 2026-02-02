import { Body, Controller, Get, Param, Post, Req } from '@nestjs/common';
import { IProject } from 'src/domain/interfaces/project.interface';
import { CreateProjectService } from 'src/domain/use-cases/projects/create-project.service';
import { GetAllProjectsService } from 'src/domain/use-cases/projects/get-all-projects.service';
import { GetProjectByIdService } from 'src/domain/use-cases/projects/get-project-by-id.service';
import { CreateProjectDto } from './dtos/create-project.dto';


const loggedUserId = 1; // Simulated logged-in user ID

@Controller('projects')
export class ProjectsController {

    constructor(
        private readonly createProjectService: CreateProjectService,
        private readonly getAllProjectService: GetAllProjectsService,
        private readonly getProjectByIdService: GetProjectByIdService,

    ) { }

    @Get()
    findAll(): Promise<IProject[]> {
        try{
            return this.getAllProjectService.execute(loggedUserId);
        }catch(error){
            throw new Error('Error fetching projects');
        }

    }

    @Get(':id')
    findById(@Req() request,@Param('id') id: number): Promise<IProject> {
        try{
            return this.getProjectByIdService.execute({projectId:id,userId:loggedUserId});
        }catch(error){
            throw new Error('Error fetching project by ID');
        }
    }

    @Post()
    create(@Req() request,@Body() createProjectDto: CreateProjectDto): Promise<IProject> {
        try{
            return this.createProjectService.execute({
                    project: createProjectDto,
                    userId: loggedUserId
                });
        }catch(error){
            throw new Error('Error creating project');
        }
    
    }
}