import { ShWidget } from 'src/widget/model/widget.model';

export interface ShPostTypeAttr {
    id: string;
    shWidget: ShWidget;
    ordinal: number;
    label: string;
}
