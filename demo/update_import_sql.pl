#!/usr/bin/perl
use strict;
use warnings;

use DateTime;
use DateTime::Format::Strptime;
use Tie::File;

my $filename = shift;
my $count = 0;
my @lines;
tie @lines, 'Tie::File', $filename or die "Can't read file: $!";

foreach	(@lines) {
	if (/insert into Performance \( show_id, date\) values \( (\d), '(.*)'\);/) {
		my $strp = DateTime::Format::Strptime->new(
			pattern => '%Y-%m-%d %H:%M:%S'
		);
		my $datetime = $strp->parse_datetime($2);
		my $dt = $datetime->add(days => 120)->strftime("%Y-%m-%d %H:%M:%S");
		$lines[$count] = "insert into Performance ( show_id, date) values ( $1, '$dt');\n";
	}
	$count++;
}

untie @lines;