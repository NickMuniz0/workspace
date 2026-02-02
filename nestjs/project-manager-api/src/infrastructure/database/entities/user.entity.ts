import { IProject } from "src/domain/interfaces/project.interface";
import { Itask } from "src/domain/interfaces/task.interface";
import { IUser } from "src/domain/interfaces/user.interface";
import { PrimaryGeneratedColumn,Column, OneToMany, Entity } from "typeorm";
import { ProjectEntity } from "./project.entity";
import { Task } from "src/domain/entites/task";
import { TaskEntity } from "./task.entity";


@Entity({name:'user'})
export class UserEntity implements IUser {
    @PrimaryGeneratedColumn()
    id: number;
    @Column({name: 'frist_name', nullable: false})
    firstName: string;
    @Column({name: 'last_name', nullable: false})
    lastName: string;
    @Column({name: 'email', nullable: false, unique: true})
    email: string;
    @Column({name: 'password', nullable: false})
    password: string;
    @OneToMany(() => ProjectEntity, (project) => project.user, {cascade:true})
    projects: IProject[];
    @OneToMany(() => TaskEntity, (task) => task.user, {cascade:true})    
    tasks: Itask[];
}