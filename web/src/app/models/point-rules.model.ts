export interface PointRule {
    pointRulesCode: string;
    winner: number;
    score: number;
    id?: number;
}

export type PointRules = PointRule[];
