import { Injectable } from '@nestjs/common';
import { ProjectsRepositoryService } from 'src/infrastructure/database/repositories/projects.repository.service';
import { UsersRepositoryService } from 'src/infrastructure/database/repositories/users.repository.service';
import { BaseUseCase } from '../base-use-case';
import { IProject } from 'src/domain/interfaces/project.interface';

@Injectable()
export class GetAllProjectsService implements BaseUseCase {

    constructor(private readonly projectRepository: ProjectsRepositoryService,
            private readonly userRepository: UsersRepositoryService
        ) { }
    async execute(userId: number): Promise<IProject[]> {
        const userData = await this.userRepository.findById(userId);

        if (!userData) {
            throw new Error('User not found');
        }
        const projects = await this.projectRepository.findAll(userData.id);        
        if (!projects) {
            throw new Error('No projects found for this user');
    
        }
        return projects;
    }
}