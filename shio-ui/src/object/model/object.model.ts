import { Breadcrumb } from 'src/folder/model/breadcrumb.model';
import { ShFolder } from 'src/folder/model/folder.model';
import { ShPost } from 'src/post/model/post.model';
import { ShSite } from 'src/repository/model/site.model';

export interface ShObject {
    breadcrumb: Breadcrumb[];
    folderpath: string;
    shFolders: ShFolder[];
    shPosts: ShPost[];
    shSite: ShSite;
}