import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { baseApiPath } from 'src/app/base-api-path';
import { Tournament, Tournaments } from '../../models/tournament.model';
import { UpdateTournament } from '../../models/update-tournament.dto';

@Injectable()
export class TournamentsService {
    private readonly tournamentApi = `${baseApiPath}/tournament`;
    private readonly httpClient = inject(HttpClient);

    getTournaments() {
        return this.httpClient.get<Tournaments>(`${this.tournamentApi}/all`);
    }

    createTournament(tournament: Tournament) {
        return this.httpClient.post<Tournament>(
            `${this.tournamentApi}/add`,
            tournament,
        );
    }

    getTournament(id: string) {
        return this.httpClient.get<Tournament>(`${this.tournamentApi}/${id}`);
    }

    updateTournament(tournament: UpdateTournament, id: string) {
        return this.httpClient.post<Tournament>(
            `${this.tournamentApi}/${id}`,
            tournament,
        );
    }

    deleteTournament(id: string) {
        return this.httpClient.delete<Tournament>(
            `${this.tournamentApi}/${id}`,
        );
    }
}
