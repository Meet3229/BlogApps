export interface IPost {
  id?: string;
  title?: string | null;
  contant?: string | null;
}

export const defaultValue: Readonly<IPost> = {};
