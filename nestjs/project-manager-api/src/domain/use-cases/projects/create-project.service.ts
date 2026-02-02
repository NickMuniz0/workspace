import { Injectable } from '@nestjs/common';
import { BaseUseCase } from '../base-use-case';
import { ProjectsRepositoryService } from 'src/infrastructure/database/repositories/projects.repository.service';
import { IProject } from 'src/domain/interfaces/project.interface';
import { CreateProjectDto } from 'src/gateways/controllers/projects/dtos/create-project.dto';
import { UsersRepositoryService } from 'src/infrastructure/database/repositories/users.repository.service';

@Injectable()
export class CreateProjectService implements BaseUseCase {
    constructor(private readonly projectRepository: ProjectsRepositoryService,
        private readonly userRepository: UsersRepositoryService
    ) { }

    async execute(payload:{ project :CreateProjectDto,userId:number}): Promise<IProject> {

        const userData = await this.userRepository.findById(payload.userId);

        if (!userData) {
            throw new Error('User not found');
        }
        
        const createProject = await this.projectRepository.add({
            name: payload.project.name,
            description: payload.project.description,
            user: {id: userData.id}
        })

        if (!createProject) {
            throw new Error('Project not created');
        }
        return createProject;
    }


}
