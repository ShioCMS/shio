import { ShPost } from './post.model';

export interface ShPostXPData {
    [x: string]: any;
    allowPublish: boolean;
    shPost: ShPost;
}