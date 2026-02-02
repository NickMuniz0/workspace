export interface BaseUseCase{
    execute(...args: any[]): Promise<any>;
}