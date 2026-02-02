import { Module } from '@nestjs/common';
import { ProjectsModule } from './projects/projects.module';
import { UsersModule } from './users/users.module';
import { TasksModule } from './tasks/tasks.module';

@Module({
  imports: [ProjectsModule, TasksModule, UsersModule],
  exports:[ProjectsModule, TasksModule, UsersModule],
})
export class UseCasesModule {}
