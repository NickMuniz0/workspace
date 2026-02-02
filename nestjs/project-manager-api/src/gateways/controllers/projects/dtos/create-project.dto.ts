import { IsNotEmpty, IsString } from "class-validator";

export class CreateProjectDto {
    @IsNotEmpty({message: "Project name must not be empty"})
    @IsString({message: "Project name must be a string"})
    name: string;
    @IsNotEmpty({message: "Project description must not be empty"})
    @IsString({message: "Project description must be a string"})
    description?: string;
}