export interface Match {
    firstTeamName: string;
    secondTeamName: string;
    startDateAndTime: string;
    firstTeamScore: number;
    secondTeamScore: number;
    finished: boolean;
    tournamentCode: string;
    id?: number;
}

export type Matches = Match[];
