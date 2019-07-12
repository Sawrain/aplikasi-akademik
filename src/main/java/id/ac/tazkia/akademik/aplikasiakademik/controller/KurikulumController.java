package id.ac.tazkia.akademik.aplikasiakademik.controller;

import id.ac.tazkia.akademik.aplikasiakademik.dao.KurikulumDao;
import id.ac.tazkia.akademik.aplikasiakademik.dao.ProdiDao;
import id.ac.tazkia.akademik.aplikasiakademik.entity.Kurikulum;
import id.ac.tazkia.akademik.aplikasiakademik.entity.Prodi;
import id.ac.tazkia.akademik.aplikasiakademik.entity.StatusRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
public class KurikulumController {

    @Autowired
    private KurikulumDao kurikulumDao;
    @Autowired
    private ProdiDao prodiDao;


    @GetMapping("/kurikulum/list")
    public void daftarKurikulum(Model model,@RequestParam(required = false) Prodi prodi,
                                @PageableDefault(direction = Sort.Direction.DESC,sort = "tahunKurikulum") Pageable page){
        model.addAttribute("prodi",prodiDao.findByStatusNotIn(StatusRecord.HAPUS));

        if (prodi != null){
            model.addAttribute("selected",prodi);
            model.addAttribute("kurikulum",kurikulumDao.findByStatusNotInAndProdi(StatusRecord.HAPUS,prodi,page));
        }
    }

    @GetMapping("/kurikulum/form")
    public void  formKurikulum(Model model,@RequestParam(required = false, name = "id")
            Kurikulum kurikulum){
        model.addAttribute("prodi",prodiDao.findByStatusNotIn(StatusRecord.HAPUS));
        model.addAttribute("kurikulum", new Kurikulum());

        if (kurikulum != null){
            model.addAttribute("kurikulum",kurikulum);
        }
    }

    @PostMapping("/kurikulum/form")
    public String prosesKurikulum(@ModelAttribute @Valid Kurikulum kurikulum, BindingResult errors){

        if(errors.hasErrors()){
            return "form";
        }

        if (kurikulum.getProdi() == null){
            List<Prodi> prodi = prodiDao.findByStatus(StatusRecord.AKTIF);
            for (Prodi p : prodi){
                Kurikulum k = new Kurikulum();
                k.setNamaKurikulum(kurikulum.getNamaKurikulum());
                k.setNomorSkRektor(kurikulum.getNomorSkRektor());
                k.setProdi(p);
                k.setTahunKurikulum(kurikulum.getTahunKurikulum());
                k.setJumlahSesi(2);
                k.setSesi("semester");
                kurikulumDao.save(k);
            }
        }

        if (kurikulum != null) {
            kurikulum.setJumlahSesi(2);
            kurikulum.setSesi("semester");
            kurikulumDao.save(kurikulum);
        }

        return "redirect:list";
    }

    @PostMapping("/kurikulum/aktif")
    public String aktifKurikulum(@RequestParam Kurikulum kurikulum){
        Kurikulum k = kurikulumDao.findByProdiAndStatus(kurikulum.getProdi(),StatusRecord.AKTIF);

        if (k != null){
            k.setStatus(StatusRecord.NONAKTIF);
            kurikulumDao.save(k);
        }

        kurikulum.setStatus(StatusRecord.AKTIF);
        kurikulumDao.save(kurikulum);

        return "redirect:list?prodi="+kurikulum.getProdi().getId();
    }
}
