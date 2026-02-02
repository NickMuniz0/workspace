import { IsNotEmpty, IsNumber, IsString } from "class-validator";

export class CreateTasksDto { 
    @IsNotEmpty({message: "Task name must not be empty"})
    @IsString({message: "Task name must be a string"})
    name: string;
    @IsNotEmpty({message: "Task status must not be empty"})
    @IsString({message: "Task status must be a string"})
    status: "pending" | "in-progress" | "completed" | undefined;
    @IsNumber({}, {message: "Task priority must be a number"})
    projectId: string;
}