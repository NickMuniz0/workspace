import { Injectable } from '@nestjs/common';
import { IUser } from 'src/domain/interfaces/user.interface';
import { UserEntity } from 'src/infrastructure/database/entities/user.entity';
import { UsersRepositoryService } from 'src/infrastructure/database/repositories/users.repository.service';

@Injectable()
export class GetUserByEmailService {

    
    constructor(
        private readonly usersRepository: UsersRepositoryService
    ) {}


    async execute(email: string): Promise<IUser|null> {
        const user =  await this.usersRepository.findByEmail(email);
        return user ;
    }
}
