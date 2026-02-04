import { ExecutionContext, Injectable, SetMetadata, CanActivate } from '@nestjs/common';
import { Reflector } from '@nestjs/core';
import { JwtService } from '@nestjs/jwt';
import { Request } from 'express';
export const IS_PUBLIC_KEY = 'isPublic';
export const Public = () => SetMetadata(IS_PUBLIC_KEY, true);

@Injectable()
export class AuthGuardService implements CanActivate {

    constructor(
        private jwtService: JwtService,
        private reflector: Reflector
    ) { }


    async canActivate(context: ExecutionContext): Promise<boolean> {
        const isPublic = this.reflector.getAllAndOverride<boolean>('isPublic', [
            context.getHandler(),
            context.getClass(),
        ]);
        if (isPublic) {
            return true;
        }

        const request = context.switchToHttp().getRequest();
        const token = this.extractTokenFromHeader(request);
        if (!token) {
            return false;
        }

        try {
            const payload = await this.jwtService.verifyAsync(token, {
                secret: process.env.JWT_SECRET,
            });
            request['user'] = payload;
        } catch (error) {
            return false;
        }

        return true;
        
    }   


    private extractTokenFromHeader(request: Request): string | undefined {
            if (!request.headers) {
                return undefined;
            }
            const [type, token] = request.headers.authorization?.split(' ') ?? [];
            return type === 'Bearer' ? token : undefined;
        }
}
