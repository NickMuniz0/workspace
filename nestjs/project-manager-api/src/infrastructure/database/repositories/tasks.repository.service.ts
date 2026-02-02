import { Injectable } from '@nestjs/common';
import { ITaskRepositories } from 'src/domain/repositories/tasks-repositories.interface';
import { Itask } from 'src/domain/interfaces/task.interface';
import { DataSource, DeepPartial, Repository } from 'typeorm';
import { TaskEntity } from '../entities/task.entity';

@Injectable()
export class TasksRepositoryService  extends Repository<TaskEntity> implements ITaskRepositories{

    constructor(dataSource: DataSource) {
        super(TaskEntity, dataSource.createEntityManager());
    }

    findAll(userId: number): Promise<Itask[]> {
        return this.findBy({user: {id: userId}});
    }

    findById(userId:number, id: number): Promise<Itask | null> {
        return this.findOneByOrFail({id, user: {id: userId}});
    }
    
    add(payload: DeepPartial<Itask>): Promise<Itask> {
        return this.save(payload);
    }


    updateById(id:number, payload: DeepPartial<Itask>) {
        return this.update(id, payload);
    }
}
