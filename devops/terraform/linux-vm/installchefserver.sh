wget -P /tmp https://packages.chef.io/files/stable/chef-server/12.17.33/ubuntu/16.04/chef-server-core_12.17.33-1_amd64.deb
sudo dpkg -i /tmp/chef-server-core_12.17.33-1_amd64.deb
sudo chef-server-ctl user-create feliperojas felipe rojas william.rojas@bizagi.com 'Bizagi123*/' --filename ~/auth.pem
sudo chef-server-ctl org-create bizdevopstest 'bizagi devops test' --association_user feliperojas --filename ~/bizdevopstest.pem
sudo chef-server-ctl reconfigure
sudo chef-server-ctl install chef-manage
sudo chef-manage-ctl reconfigure
#add dns name to path /etc/opscode/chef-server.rb => api_fqdn "chefbiztest.eastus.cloudapp.azure.com"
# add in /etc/hosts => <public ip> <dns name> <hostname>
# sudo chef-server-ctl reconfigure
## put this in /etc/opscode/chef-server.rb
#server_name = "chefbiztest.eastus.cloudapp.azure.com"
#api_fqdn = server_name
#bookshelf['vip'] = server_name
#nginx['url'] = "https://#{server_name}"
#nginx['server_name'] = server_name
#nginx['ssl_certificate'] = "/var/opt/chef-server/nginx/ca/#{server_name}.crt"
#nginx['ssl_certificate_key'] = "/var/opt/chef-server/nginx/ca/#{server_name}.key"
#lb['fqdn'] = server_name