import { IProject } from "src/domain/interfaces/project.interface";
import { Itask } from "src/domain/interfaces/task.interface";
import type { IUser } from "src/domain/interfaces/user.interface";
import { Column, Entity, JoinColumn, ManyToOne, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { TaskEntity } from "./task.entity";
import { UserEntity } from "./user.entity";


@Entity({name:'project'})
export class ProjectEntity implements IProject {
    @PrimaryGeneratedColumn()
    id: number;
    @Column({name: 'name',nullable: false})
    name: string;
    @Column({name: 'description',nullable: false})
    description: string;
    @ManyToOne(() => UserEntity, (user) => user.projects, {nullable:false})
    @JoinColumn()    
    user: IUser;
    @OneToMany(() => TaskEntity, (task) => task.project, {cascade:true})
    tasks: Itask[];
}