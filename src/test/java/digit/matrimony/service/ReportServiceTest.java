package digit.matrimony.service;

import digit.matrimony.dto.ReportRequestDTO;
import digit.matrimony.dto.ReportResponseDTO;
import digit.matrimony.entity.Report;
import digit.matrimony.entity.Role;
import digit.matrimony.entity.Subscription;
import digit.matrimony.entity.User;
import digit.matrimony.exception.BadRequestException;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.ReportMapper;
import digit.matrimony.repository.ReportRepository;
import digit.matrimony.repository.SubscriptionRepository;
import digit.matrimony.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @InjectMocks
    private ReportService reportService;

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Spy
    private ReportMapper reportMapper = new ReportMapper(); // assuming static methods

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User createUser(Long id, short roleId) {
        User user = new User();
        user.setId(id);
        user.setRole(new Role(roleId, "Role" + roleId));
        return user;
    }


    private ReportRequestDTO createValidRequest() {
        ReportRequestDTO dto = new ReportRequestDTO();
        dto.setReporterId(1L);
        dto.setReportedUserId(2L);
        dto.setReason("Fake profile");
        return dto;
    }

    @Test
    void testGetReportById_Success() {
        User reporter = new User();
        reporter.setId(1L);
        reporter.setRole(new Role((short) 3, "User"));

        User reportedUser = new User();
        reportedUser.setId(2L);
        reportedUser.setRole(new Role((short) 3, "User"));

        User reviewer = new User();
        reviewer.setId(3L);
        reviewer.setRole(new Role((short) 2, "Manager"));

        Report report = Report.builder()
                .id(1L)
                .reporter(reporter)
                .reportedUser(reportedUser)
                .reviewedByUser(reviewer)
                .reason("Fake profile")
                .status("open")
                .escalatedToAdmin(false)
                .reportedAt(LocalDateTime.now())
                .build();

        when(reportRepository.findById(1L)).thenReturn(Optional.of(report));

        ReportResponseDTO dto = reportService.getReportById(1L);

        assertEquals(1L, dto.getId());
        assertEquals("Fake profile", dto.getReason());
        assertEquals(1L, dto.getReporterId());
        assertEquals(2L, dto.getReportedUserId());
        assertEquals(3L, dto.getReviewedByUserId());
    }


    @Test
    void testGetReportById_NotFound() {
        when(reportRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reportService.getReportById(1L));
    }

    @Test
    void testCreateReport_Success() {
        ReportRequestDTO dto = createValidRequest();
        User reporter = createUser(1L, (short) 3);
        User reportedUser = createUser(2L, (short) 3);
        User manager = createUser(3L, (short) 2);
        User admin = createUser(4L, (short) 1);

        when(reportRepository.findDuplicateReport(1L, 2L, "Fake profile")).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(reporter));
        when(userRepository.findById(2L)).thenReturn(Optional.of(reportedUser));
        when(userRepository.findAll()).thenReturn(List.of(manager, admin));
        Subscription mockSubscription = new Subscription(); // assuming default constructor
        when(subscriptionRepository.findActiveSubscriptionByUserId(1L)).thenReturn(Optional.of(mockSubscription));

        when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReportResponseDTO response = reportService.createReport(dto);
        assertNotNull(response);
        assertEquals("Fake profile", response.getReason());
    }

    @Test
    void testCreateReport_Duplicate() {
        ReportRequestDTO dto = createValidRequest();
        when(reportRepository.findDuplicateReport(1L, 2L, "Fake profile"))
                .thenReturn(Optional.of(new Report()));
        assertThrows(IllegalArgumentException.class, () -> reportService.createReport(dto));
    }

    @Test
    void testCreateReport_SameUser() {
        ReportRequestDTO dto = new ReportRequestDTO();
        dto.setReporterId(1L);
        dto.setReportedUserId(1L);
        dto.setReason("Spam");
        assertThrows(BadRequestException.class, () -> reportService.createReport(dto));
    }

    @Test
    void testCreateReport_InvalidReporterRole() {
        ReportRequestDTO dto = createValidRequest();
        User reporter = createUser(1L, (short) 2); // Not role 3
        when(reportRepository.findDuplicateReport(1L, 2L, "Fake profile")).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(reporter));
        assertThrows(BadRequestException.class, () -> reportService.createReport(dto));
    }

    @Test
    void testUpdateReport_Success() {
        ReportRequestDTO dto = createValidRequest();
        Report existing = Report.builder().id(1L).build();
        User reporter = createUser(1L, (short) 3);
        User reportedUser = createUser(2L, (short) 3);
        User manager = createUser(3L, (short) 2);

        when(reportRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.findById(1L)).thenReturn(Optional.of(reporter));
        when(userRepository.findById(2L)).thenReturn(Optional.of(reportedUser));
        when(userRepository.findAll()).thenReturn(List.of(manager));
        when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReportResponseDTO response = reportService.updateReport(1L, dto);
        assertNotNull(response);
        assertEquals("Fake profile", response.getReason());
    }

    @Test
    void testDeleteReport_Success() {
        Report report = Report.builder().id(1L).build();
        when(reportRepository.findById(1L)).thenReturn(Optional.of(report));
        reportService.deleteReport(1L);
        verify(reportRepository).delete(report);
    }

    @Test
    void testDeleteReport_NotFound() {
        when(reportRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reportService.deleteReport(1L));
    }
}
