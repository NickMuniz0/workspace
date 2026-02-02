import { Module } from '@nestjs/common';
import { UseCasesModule } from 'src/domain/use-cases/use-cases.module';
import { ProjectsController } from './projects/projects.controller';
import { UsersController } from './users/users.controller';
import { TasksController } from './tasks/tasks.controller';


@Module({
    imports: [UseCasesModule],
    controllers: [ProjectsController,TasksController,UsersController],
})
export class ControllersModule {}
