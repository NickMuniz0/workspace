import { Module } from '@nestjs/common';
import { ProjectsRepositoryService } from './repositories/projects.repository.service';
import { TasksRepositoryService } from './repositories/tasks.repository.service';
import { UsersRepositoryService } from './repositories/users.repository.service';
import { TypeOrmModule } from '@nestjs/typeorm';
import { TaskEntity } from './entities/task.entity';
import { ProjectEntity } from './entities/project.entity';
import { UserEntity } from './entities/user.entity';

@Module({imports: [
    TypeOrmModule.forFeature([TaskEntity,ProjectEntity,UserEntity]),
    TypeOrmModule.forRoot({
    type:"sqlite",
    database:"db/sql.sqlite",
    entities:["dist/**/*.entity{.ts,.js}"],
    synchronize:true,
    autoLoadEntities:true
})],
  providers: [ProjectsRepositoryService, TasksRepositoryService, UsersRepositoryService],
  exports: [ProjectsRepositoryService, TasksRepositoryService, UsersRepositoryService],
})
export class DatabaseModule {}
