import { IsNotEmpty, IsString, IsStrongPassword } from "class-validator";

export class CreateUsersDto {
    @IsNotEmpty({message: "First name must not be empty"})
    @IsString({message: "First name must be a string"})
    firstName?: string;
    @IsNotEmpty({message: "Last name must not be empty"})
    @IsString({message: "Last name must be a string"})
    lastName?: string; 
    @IsNotEmpty({message: "Email must not be empty"})
    @IsString({message: "Email must be a string"})
    email: string;
    // @IsStrongPassword
    password: string;
}