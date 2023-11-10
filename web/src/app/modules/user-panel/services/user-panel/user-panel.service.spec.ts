import { UserPanelService } from './user-panel.service';
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe(UserPanelService.name, (): void => {
    beforeEach((): void => {
        TestBed.configureTestingModule({
            providers: [UserPanelService],
            imports: [HttpClientTestingModule],
        });
    });

    it('hello', (): void => {
        expect(true).toBeTruthy();
    });
});
