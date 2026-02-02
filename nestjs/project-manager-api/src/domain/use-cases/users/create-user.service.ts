import { Injectable } from '@nestjs/common';
import { BaseUseCase } from '../base-use-case';
import { CreateUsersDto } from 'src/gateways/controllers/users/dtos/create-users.dto';
import { IUser } from 'src/domain/interfaces/user.interface';
import { UsersRepositoryService } from 'src/infrastructure/database/repositories/users.repository.service';

@Injectable()
export class CreateUserService implements BaseUseCase {


    constructor(private readonly userRepository: UsersRepositoryService) { }

    async execute(user:CreateUsersDto): Promise<IUser> {
        console.log('Creating user with data:', user);
        const createdUser = await this.userRepository.add(user); 
        if (!createdUser) {
            throw new Error('User could not be created');
        }
        return createdUser;
    }
}
