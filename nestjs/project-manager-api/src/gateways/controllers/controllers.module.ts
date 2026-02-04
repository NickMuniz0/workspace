import { Module } from '@nestjs/common';
import { UseCasesModule } from 'src/domain/use-cases/use-cases.module';
import { ProjectsController } from './projects/projects.controller';
import { UsersController } from './users/users.controller';
import { TasksController } from './tasks/tasks.controller';
import { AuthController } from './auth/auth.controller';
import { AuthModule } from 'src/infrastructure/auth/auth.module';


@Module({
    imports: [UseCasesModule,AuthModule],
    controllers: [ProjectsController,TasksController,UsersController, AuthController],
})
export class ControllersModule {}
