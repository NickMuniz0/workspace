import { Injectable } from '@nestjs/common';
import { BaseUseCase } from '../base-use-case';
import { CreateUsersDto } from 'src/gateways/controllers/users/dtos/create-users.dto';
import { IUser } from 'src/domain/interfaces/user.interface';
import { UsersRepositoryService } from 'src/infrastructure/database/repositories/users.repository.service';
import { hash } from 'bcrypt';


@Injectable()
export class CreateUserService implements BaseUseCase {

    private readonly  DEFAULT_SALT_ROUNDS = 10;
    constructor(private readonly userRepository: UsersRepositoryService) { }

    async execute(user:CreateUsersDto): Promise<IUser> {
        const hashedPassword =await hash(user.password,this.DEFAULT_SALT_ROUNDS)


        console.log('Creating user with data:', user);
        const createdUser = await this.userRepository.add({...user, password: hashedPassword}); 
        if (!createdUser) {
            throw new Error('User could not be created');
        }
        return createdUser;
    }
}
