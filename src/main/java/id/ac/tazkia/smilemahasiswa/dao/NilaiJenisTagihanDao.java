package id.ac.tazkia.smilemahasiswa.dao;

import id.ac.tazkia.smilemahasiswa.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface NilaiJenisTagihanDao extends PagingAndSortingRepository<NilaiJenisTagihan, String> {

    Page<NilaiJenisTagihan> findByStatusNotInAndJenisTagihanContainingIgnoreCaseOrderByJenisTagihan(List<StatusRecord> asList, String search, Pageable page);

    Page<NilaiJenisTagihan> findByStatusNotIn(List<StatusRecord> asList, Pageable page);

    List<NilaiJenisTagihan> findByStatusOrderByJenisTagihan(StatusRecord statusRecord);

    Integer countByStatusAndJenisTagihan(StatusRecord statusRecord, JenisTagihan jenisTagihan);

    List<NilaiJenisTagihan> findByAngkatanAndProdiAndStatus(String angkatan , Prodi prodi, StatusRecord statusRecord);

}
