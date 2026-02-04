import { Injectable, UnauthorizedException } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import { compare } from 'bcrypt';
import { GetUserByEmailService } from 'src/domain/use-cases/users/get-user-by-email.service';

@Injectable()
export class AuthService {


    constructor(
        private readonly jwtService: JwtService,
        private readonly  getUserByEmail: GetUserByEmailService
    ) {}

    async login(email: string, password: string) {
        if (!email || !password) {
            throw new UnauthorizedException('Invalid credentials');
        }

        const user = await this.getUserByEmail.execute(email);
        if (!user || !user.password) {
            throw new UnauthorizedException('Invalid credentials');
        }

        const isAValidUser = await compare(password, user.password);

        if (!isAValidUser) {
            throw new UnauthorizedException('Invalid credentials');
        }

        const payload = { email: user.email, sub: user.id };
        const token = await this.jwtService.signAsync(payload);
        return {
            access_token: token,

}

}
}
