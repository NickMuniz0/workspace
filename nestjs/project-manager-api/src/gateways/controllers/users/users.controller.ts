import { Body, Controller, Get, Param, Post } from '@nestjs/common';
import { CreateUserService } from 'src/domain/use-cases/users/create-user.service';
import { GetUserByIdService } from 'src/domain/use-cases/users/get-user-by-id.service';
import { CreateUsersDto } from './dtos/create-users.dto';

@Controller('users')
export class UsersController {

    constructor(

        private readonly getUserByIdUseCase: GetUserByIdService,
        private readonly createUserUseCase: CreateUserService
    ) {}

    @Get(':id')
    async findOne(@Param ('id') id: number) {
        try{
            return this.getUserByIdUseCase.execute(id);
        }catch(error){
            console.log(error);
        }
    }

    @Post()
    async create(@Body() createUsersDto: CreateUsersDto) {
        try{
            return this.createUserUseCase.execute(createUsersDto);
        }catch(error){
            console.log(error);
        }
    }

}
