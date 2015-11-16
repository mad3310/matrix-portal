$(function () {
	getOverview();
});

function getOverview(){
	$.ajax({
		cache:false,
		type : "get",
		url : "/dashboard/statistics",
		contentType : "application/json; charset=utf-8",
		success : function(data) {
			if(error(data)) return;
			var view = data.data;
			$('#db_hclusterSum').html(view.db_hclusterSum);
			$('#db_hostSum').html(view.db_hostSum);
			$('#db_clusterSum').html(view.db_clusterSum);
			$('#db_dbSum').html(view.db_dbSum);
			$('#db_unauditeDbSum').html(view.db_unauditeDbSum);

			$('#gce_hclusterSum').html(view.gce_hclusterSum);
			$('#gce_hostSum').html(view.gce_hostSum);
			$('#gce_clusterSum').html(view.gce_clusterSum);
			$('#gce_gceSum').html(view.gce_gceSum);
			$('#gce_unauditeGceSum').html(view.gce_unauditeGceSum);

            $('#slb_hclusterSum').html(view.slb_hclusterSum);
            $('#slb_hostSum').html(view.slb_hostSum);
            $('#slb_clusterSum').html(view.slb_clusterSum);
            $('#slb_slbSum').html(view.slb_slbSum);
            $('#slb_unauditeSlbSum').html(view.slb_unauditeSlbSum);

            $('#ocs_hclusterSum').html(view.ocs_hclusterSum);
            $('#ocs_hostSum').html(view.ocs_hostSum);
            $('#ocs_clusterSum').html(view.ocs_clusterSum);
            $('#ocs_ocsSum').html(view.ocs_ocsSum);
            $('#ocs_unauditeOcsSum').html(view.ocs_unauditeOcsSum);

            $('#oss_hclusterSum').html(view.oss_hclusterSum);
            $('#oss_hostSum').html(view.oss_hostSum);
            $('#oss_clusterSum').html(view.oss_clusterSum);
            $('#oss_ossSum').html(view.oss_ossSum);
            $('#oss_unauditeOssSum').html(view.oss_unauditeOssSum);
		},
});
}
