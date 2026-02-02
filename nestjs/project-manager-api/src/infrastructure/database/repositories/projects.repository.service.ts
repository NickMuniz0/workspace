import { Injectable } from '@nestjs/common';
import { DeepPartial, Repository } from 'typeorm';
import { ProjectEntity } from '../entities/project.entity';
import { IProjectsRepositories } from 'src/domain/repositories/projects-repositories.interface';
import { IProject } from 'src/domain/interfaces/project.interface';
import { DataSource } from 'typeorm';
@Injectable()
export class ProjectsRepositoryService extends Repository<ProjectEntity> implements IProjectsRepositories{

    constructor(dataSource: DataSource) {
        super(ProjectEntity, dataSource.createEntityManager());
    }

    findAll(userId: number): Promise<IProject[]> {
        return this.findBy({user: {id: userId}});
    }
    findById(userid:number, id: number): Promise<IProject| null> {
       return this.findOneByOrFail({id, user: {id: userid}});
    }
    add(payload: DeepPartial<IProject>): Promise<IProject> {
        return this.save(payload);
    }
}
