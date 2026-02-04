import { Module } from '@nestjs/common';
import { ControllersModule } from './controllers/controllers.module';
import { AuthGuardService } from './guards/auth-guard/auth-guard.service';
import { AuthModule } from '../infrastructure/auth/auth.module';
import { APP_GUARD } from '@nestjs/core';

@Module({
  imports: [ControllersModule, AuthModule],
  providers: [AuthGuardService, {
    provide: APP_GUARD,
    useClass: AuthGuardService,
  }],
  exports: [AuthGuardService]
})
export class GatewaysModule {}
