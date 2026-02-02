import { Injectable } from '@nestjs/common';
import { BaseUseCase } from '../base-use-case';
import { ProjectsRepositoryService } from 'src/infrastructure/database/repositories/projects.repository.service';
import { UsersRepositoryService } from 'src/infrastructure/database/repositories/users.repository.service';
import { IProject } from 'src/domain/interfaces/project.interface';

@Injectable()
export class GetProjectByIdService implements BaseUseCase {

    constructor(private readonly projectRepository: ProjectsRepositoryService,
            private readonly userRepository: UsersRepositoryService
        ) { }
    async execute(payload: { userId: number, projectId: number }): Promise<IProject> {
        const userData = await this.userRepository.findById(payload.userId);

        if (!userData) {
            throw new Error('User not found');
        }

        const project = await this.projectRepository.findById(payload.userId, payload.projectId);

        if (!project) {
            throw new Error('Project not found');
        }

        return project;
    }
}
