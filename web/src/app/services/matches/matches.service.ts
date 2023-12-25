import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { baseApiPath } from 'src/app/base-api-path';
import { Match, Matches } from '../../models/match.model';

@Injectable()
export class MatchesService {
    private readonly matchesApi = `${baseApiPath}/match`;
    private readonly httpClient = inject(HttpClient);

    getMatches() {
        return this.httpClient.get<Matches>(`${this.matchesApi}/all`);
    }

    createMatch(match: Match) {
        return this.httpClient.post<Match>(`${this.matchesApi}/add`, match);
    }

    getMatch(id: string) {
        return this.httpClient.get<Match>(`${this.matchesApi}/${id}`);
    }

    updateMatch(match: Match, id: string) {
        return this.httpClient.post<Match>(`${this.matchesApi}/${id}`, match);
    }

    deleteMatch(id: number) {
        return this.httpClient.delete<Match>(`${this.matchesApi}/${id}`);
    }
}
