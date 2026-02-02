import { Itask } from "src/domain/interfaces/task.interface";
import type { IUser } from "src/domain/interfaces/user.interface";
import { Column, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn } from "typeorm";
import { ProjectEntity } from "./project.entity";
import type { IProject } from "src/domain/interfaces/project.interface";
import { UserEntity } from "./user.entity";

@Entity({name:'task'})
export class TaskEntity implements Itask {
    @PrimaryGeneratedColumn()
    id: number;
    @Column({name: 'name',nullable: false})
    name: string;
    @Column({name: 'status',nullable: false})
    status: "pending" | "in-progress" | "completed" ;
    @ManyToOne(() => ProjectEntity, (project) => project.tasks, {nullable:false})
    project: IProject;
    @ManyToOne(() => UserEntity, (user) => user.tasks, {nullable:false})
    @JoinColumn()
    user: IUser;

}