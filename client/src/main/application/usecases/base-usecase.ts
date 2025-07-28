interface IBaseUseCase<T, K> {
  execute(input: T): Promise<K>;
}

export abstract class AbstractBaseUseCase<T, K> implements IBaseUseCase<T, K> {
  abstract execute(input: T): Promise<K>;
}
