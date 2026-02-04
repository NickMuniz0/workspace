import { Injectable } from '@nestjs/common';
import { UserEntity } from '../entities/user.entity';
import { IUsersRepositories } from 'src/domain/repositories/users-repositories.interface';
import { DataSource, DeepPartial, Repository } from 'typeorm';
import { IUser } from 'src/domain/interfaces/user.interface';

@Injectable()
export class UsersRepositoryService  extends Repository<UserEntity> implements IUsersRepositories{

    constructor(dataSource: DataSource) {
        super(UserEntity, dataSource.createEntityManager());
    }
    findAll(userId: number): Promise<IUser[]> {
         return this.findBy({id: userId});
     }

     findById(id: number): Promise<IUser | null> {
         return this.findOneBy({id});
     }
     add(payload: DeepPartial<IUser>): Promise<IUser> {
         return this.save(payload);
     }

     updateById(id:number, payload: DeepPartial<IUser>) {
         return this.update(id, payload);
     }
     findByEmail(email: string): Promise<IUser | null> {
         return this.findOneBy({email});
     }
}