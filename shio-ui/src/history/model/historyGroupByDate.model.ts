import { ShHistory } from './history.model';

export interface ShHistoryGroup {
    groupByDay: unknown;
    day: Date;
    histories: ShHistory[];
}