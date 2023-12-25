export interface Tournament {
    tournamentName: string;
    tournamentCode: string;
    pointRulesCode: string;
    id?: number;
}

export type Tournaments = Tournament[];
