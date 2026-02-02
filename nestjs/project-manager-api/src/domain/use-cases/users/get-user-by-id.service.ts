import { Injectable } from '@nestjs/common';
import { BaseUseCase } from '../base-use-case';
import { UsersRepositoryService } from 'src/infrastructure/database/repositories/users.repository.service';

@Injectable()
export class GetUserByIdService implements BaseUseCase {
    constructor(private readonly userRepository: UsersRepositoryService) { }

    async execute(id: number): Promise<any> {
        const user = await this.userRepository.findById(id);
        if (!user) {
            throw new Error('User not found');
        }
        return user;
    }
}