import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { DomainModule } from './domain/domain.module';
import { InfrastructureModule } from './infrastructure/infrastructure.module';
import { GatewaysModule } from './gateways/gateways.module';

@Module({
  imports: [DomainModule, InfrastructureModule, GatewaysModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
