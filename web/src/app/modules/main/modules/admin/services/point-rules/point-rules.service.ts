import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { baseApiPath } from 'src/app/base-api-path';
import { PointRule, PointRules } from '../../models/point-rules.model';

@Injectable()
export class PointRulesService {
    private readonly pointRulesApi = `${baseApiPath}/pointrules`;
    private readonly httpClient = inject(HttpClient);

    getPointRules() {
        return this.httpClient.get<PointRules>(`${this.pointRulesApi}/all`);
    }

    createPointRule(pointRule: PointRule) {
        return this.httpClient.post<PointRule>(
            `${this.pointRulesApi}/add`,
            pointRule,
        );
    }

    getPointRule(id: string) {
        return this.httpClient.get<PointRule>(`${this.pointRulesApi}/${id}`);
    }

    updatePointRule(pointRule: PointRule, id: string) {
        return this.httpClient.post<PointRule>(
            `${this.pointRulesApi}/${id}`,
            pointRule,
        );
    }

    deletePointRule(id: string) {
        return this.httpClient.delete<PointRule>(`${this.pointRulesApi}/${id}`);
    }
}
